package models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    @NotBlank
    @Size(min = 5, max = 20)
    private String username;

    @NotBlank
    @Pattern(regexp = "^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$", message = "password must be encrypted (bcrypt)")
    private String password;

    @ManyToMany
    @JoinTable(name = "users_locations",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    @Builder.Default
    private Set<Location> locations = new HashSet<>();

    public void addLocation(Location location) {
        locations.add(location);
        location.getUsers().add(this);
    }

    public void removeLocation(Location location) {
        locations.remove(location);
        location.getUsers().remove(this);
    }
}
