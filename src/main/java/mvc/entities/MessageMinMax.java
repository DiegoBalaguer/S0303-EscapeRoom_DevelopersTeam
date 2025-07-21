package mvc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageMinMax {
    private String message;
    private int minium;
    private int maxium;
}
