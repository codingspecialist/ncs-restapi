package shop.mtcoding.blog.course.web.dto;

import shop.mtcoding.blog.course.application.port.in.dto.SubjectElementCommand;

public class SubjectElementRequest {

    public record Save(
            Integer no,
            String title,
            String criterion
    ) {
        public SubjectElementCommand.Save toCommand() {

            return new SubjectElementCommand.Save(
                    this.no,
                    this.title,
                    this.criterion
            );

        }
    }
}