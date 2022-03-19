package myBlog.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardUpdateDto {
    private Long boardId;
    private Long userId;
    private String content;
    private String title;
}
