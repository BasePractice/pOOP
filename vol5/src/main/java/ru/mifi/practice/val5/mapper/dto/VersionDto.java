package ru.mifi.practice.val5.mapper.dto;

import java.time.LocalDateTime;


@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
public final class VersionDto {
    private String version;
    private LocalDateTime releaseDate;

    public void setVersion(String version) {
        this.version = version;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }
}
