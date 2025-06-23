import { CourseBasicDTO } from "./courseBasic.dto";
import { UserBasicDTO } from "./userBasic.dto";

export interface CommentDTO {
    id: number;
    course: CourseBasicDTO;
    user: UserBasicDTO;
    createdDate: string;
    text: string;
}