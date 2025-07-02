package mvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Getter;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Element{
    private int id;
    private String name;
    private String description;
    private boolean isActive;

}
