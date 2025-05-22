package shop.mtcoding.blog.user.teacher;

import lombok.Data;

public class TeacherResponse {

    @Data
    public static class DTO {
        private Long id;
        private Long userId;
        private String username;
        private String sign;
        private String name;

        public DTO(Teacher teacher) {
            this.id = teacher.getId();
            this.userId = teacher.getUser().getId();
            this.username = teacher.getUser().getUsername();
            String key = teacher.getUser().getUsername().length() > 2
                    ? username.substring(0, 2) + "**"
                    : username + "**";
            this.sign = teacher.getSign();
            this.name = teacher.getName() + "(" + key + ")";
        }
    }
}
