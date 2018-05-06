package repository.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import repository.enums.EnumBerechtigungslevel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Berechtigungslevel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private long id;

    @NotNull(message = "Das Berechtigungslevel benötigt eine Beschreibung")
    @Enumerated(EnumType.STRING)
    private EnumBerechtigungslevel beschreibung;

    @OneToMany(mappedBy = "berechtigungslevel", fetch = FetchType.LAZY)
    private List<Benutzer> benutzer;

}
