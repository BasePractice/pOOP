package ru.mifi.practice.vol6.menu;

import ru.mifi.practice.vol6.Menu;
import ru.mifi.practice.vol6.model.User;
import ru.mifi.practice.vol6.repository.Repository;

public final class DeleteRegistrationMenu extends AbstractMenu {
    private final Repository.Mutant<User, String> repository;

    public DeleteRegistrationMenu(Repository.Mutant<User, String> repository) {
        super("Удалить");
        this.repository = repository;
    }

    @Override
    public void accept(Menu.Context context) {
        context.authorized().ifPresent(ctx -> {
            String select = context.select("Вы уверены что хотите удалить?[y/N]");
            if ("y".equalsIgnoreCase(select)) {
                repository.delete(ctx.user().username());
                context.clearContext();
            }
        });
    }
}
