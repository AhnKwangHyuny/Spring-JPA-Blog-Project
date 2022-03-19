package myBlog.core.Domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@MappedSuperclass
@Data
public abstract class BaseEntity {

    @CreationTimestamp // 시간이 자동입력됨
    private Timestamp createdDate; // 생성 시간

    private Timestamp updatedDate; // 수정 시간
}
