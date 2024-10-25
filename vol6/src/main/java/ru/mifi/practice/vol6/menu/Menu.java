package ru.mifi.practice.vol6.menu;

import ru.mifi.practice.vol6.Output;

import java.util.ArrayList;
import java.util.List;
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
        output.println("---------------------");
    }

    public static Context defaultContext(Runnable onExit) {
        return new Context(onExit);
    }

    public Menu addSub(String text, Consumer<Context> action) {
        return addSub(new Menu(text, this, action));
    }

    public Menu addSub(Menu menu) {
        subMenus.add(menu);
        return menu;
    }

    public void select(Context context) {
        if (action != null) {
            action.accept(context);
        } else {
            selectedSubMenu(context);
        }
    }

    private void selectedSubMenu(Context context) {
        boolean select = true;
        while (select) {
            printLine(context);
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
                context.exit();
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

}
