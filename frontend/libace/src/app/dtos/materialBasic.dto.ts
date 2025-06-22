import { CourseBasicDTO } from "./courseBasic.dto";

export interface MaterialBasicDTO {
    id: number | null;
    name: string;
    type: string;
    url: string;
    course: CourseBasicDTO | null;
}
