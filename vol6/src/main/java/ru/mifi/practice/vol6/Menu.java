package ru.mifi.practice.vol6;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@SuppressWarnings("PMD.UnusedPrivateMethod")
public final class Menu {
    private final String text;
    private final Menu parent;
    private final Consumer<Context> action;
    private final List<Menu> subMenus;

    private Menu(String text, Menu parent, List<Menu> subMenus, Consumer<Context> action) {
        this.text = text;
        this.parent = parent;
        this.subMenus = subMenus;
        this.action = action;
    }

    private Menu(String text, Menu parent) {
        this(text, parent, new ArrayList<>(), null);
    }

    private Menu(String text, Menu parent, Consumer<Context> action) {
        this(text, parent, new ArrayList<>(), action);
    }

    public static Menu root() {
        return new Menu(null, null);
    }

    private static void printLine(Output output) {
        output.println("----------------");
    }

    public static Context defaultContext(Runnable onExit) {
        return new Context(onExit);
    }

    public Menu addSub(String text, Consumer<Context> action) {
        Menu menu = new Menu(text, this, action);
        subMenus.add(menu);
        return menu;
    }

    void select(Context context) {
        if (action != null) {
            action.accept(context);
        } else {
            selectedSubMenu(context);
        }
    }

    private void selectedSubMenu(Context context) {
        boolean select = true;
        while (select) {
            context.print();
            printLine(context);
            for (int i = 0; i < subMenus.size(); i++) {
                Menu sub = subMenus.get(i);
                context.println("%4d: %s", i + 1, sub.text);
            }
            context.println("exit: Выход");
            if (parent != null) {
                context.println("up  : Наверх");
            }
            printLine(context);
            context.print("> ");
            String in = context.inputString();
            if ("exit".equals(in)) {
                context.onExit.run();
                System.exit(0);
            } else if (parent != null && "up".equals(in)) {
                select = false;
                parent.select(context);
            } else {
                try {
                    int index = Integer.parseInt(in) - 1;
                    if (index >= 0 && index < subMenus.size()) {
                        subMenus.get(index).select(context);
                    } else {
                        context.errorln("Не верный номер. %s", in);
                    }
                } catch (Exception ex) {
                    context.errorln("Ошибка ввода. %s", in);
                }
            }
        }
    }

    public static final class Context implements Output, Input {
        private final Output output;
        private final Input input;
        private final AtomicReference<Authentication.Context> holder;
        private final Runnable onExit;

        public Context(Output output, Input input, Runnable onExit) {
            this.output = output;
            this.input = input;
            this.holder = new AtomicReference<>();
            this.onExit = onExit;
        }

        private Context(Runnable onExit) {
            this(Output.DEFAULT, Input.DEFAULT, onExit);
        }


        @Override
        public void close() throws Exception {
            input.close();
        }

        @Override
        public String inputString() {
            return input.inputString();
        }

        @Override
        public Optional<Number> inputNumber() {
            return input.inputNumber();
        }

        @Override
        public void print(String format, Object... args) {
            output.print(format, args);
        }

        void print() {
            String authenticated = "не авторизован";
            Authentication.Context context = holder.get();
            if (context != null) {
                authenticated = context.user().username();
            }
            output.println("%s", authenticated);
        }

        @Override
        public void error(String format, Object... args) {
            output.error(format, args);
        }

        public String select(String text) {
            output.println(text);
            output.print("> ");
            return input.inputString();
        }

        void putContext(Authentication.Context context) {
            holder.compareAndExchange(null, context);
        }

        void clearContext() {
            holder.setRelease(null);
        }
    }
}
