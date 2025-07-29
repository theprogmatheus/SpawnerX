package com.github.theprogmatheus.mc.plugin.spawnerx.util;

import org.jetbrains.annotations.NotNull;

/**
 * Interface genérica para mapeamento bidirecional entre dois tipos: {@code FROM} e {@code TO}.
 *
 * @param <FROM> Tipo de origem (geralmente o modelo de domínio ou DTO).
 * @param <TO>   Tipo de destino (geralmente a entidade de persistência ou vice-versa).
 */
public interface ObjectMapper<FROM, TO> {

    /**
     * Converte uma instância do tipo {@code FROM} para uma instância do tipo {@code TO}.
     *
     * @param from objeto do tipo {@code FROM} a ser convertido.
     * @return objeto convertido do tipo {@code TO}.
     */
    TO mapTo(@NotNull FROM from);

    /**
     * Converte uma instância do tipo {@code TO} para uma instância do tipo {@code FROM}.
     *
     * @param to objeto do tipo {@code TO} a ser convertido.
     * @return objeto convertido do tipo {@code FROM}.
     */
    FROM mapFrom(@NotNull TO to);
}