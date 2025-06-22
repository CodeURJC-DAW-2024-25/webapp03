import { CourseBasicDTO } from "./courseBasic.dto";

export interface MaterialDTO {
    id: number | null;
    name: string;
    type: string;
    url: string;
    Blob: Blob | null;
    course: CourseBasicDTO | null;

}