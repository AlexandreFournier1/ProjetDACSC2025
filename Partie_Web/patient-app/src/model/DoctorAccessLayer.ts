import type { Doctor } from "./entity/Doctor";
import type { DoctorVM } from "./viewmodel/DoctorVM";

export interface DoctorAccessLayer {
    load(doctorVM?: DoctorVM): Promise<Doctor[]>
}