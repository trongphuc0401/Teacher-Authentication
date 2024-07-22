package vn.edu.likelion.TeacherAuthen.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Teacher -
 *
 * @param
 * @return
 * @throws
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    private long id;
    private String name;
    private Role roleId;
    private String password;
    private String email;
}
