package ru.mifi.practice.vol6.menu;

import ru.mifi.practice.vol6.model.User;
import ru.mifi.practice.vol6.repository.RepositoryMutant;

public final class DeleteRegistrationMenu extends AbstractMenu {
    private final RepositoryMutant<User, String> repository;

    public DeleteRegistrationMenu(RepositoryMutant<User, String> repository) {
        super("Удалить");
        this.repository = repository;
    }

    @Override
    public void accept(Context context) {
        context.authorized().ifPresent(ctx -> {
            String select = context.select("Вы уверены что хотите удалить?[y/N]");
            if ("y".equalsIgnoreCase(select)) {
                repository.delete(ctx.user().username());
                context.clearSession();
            }
        });
    }
}
