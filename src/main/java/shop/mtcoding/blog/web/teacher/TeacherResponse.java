package shop.mtcoding.blog.web.teacher;

import lombok.Data;
import shop.mtcoding.blog.domain.user.teacher.Teacher;

public class TeacherResponse {

    @Data
    public static class DTO {
        private Long id;
        private Long userId;
        private String username;
        private String sign;
        private String nameAndUsername;
        private String teacherName;

        public DTO(Teacher teacher) {
            this.id = teacher.getId();
            this.userId = teacher.getUser().getId();
            this.username = teacher.getUser().getUsername();
            String key = teacher.getUser().getUsername().length() > 2
                    ? username.substring(0, 3) + "**"
                    : username + "**";
            this.sign = teacher.getSign();
            this.nameAndUsername = teacher.getName() + "(" + key + ")";
            this.teacherName = teacher.getName();
        }
    }

}
