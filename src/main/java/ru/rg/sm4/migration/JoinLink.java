package ru.rg.sm4.migration;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JoinLink {
    private String column;
    private String with;
}
