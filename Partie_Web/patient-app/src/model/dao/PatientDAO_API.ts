import type { PatientVM } from "@/viewmodel/PatientVM";
import type { Patient } from "../entity/Patient";
import type { PatientAccessLayer } from "../PatientAccessLayer";

export class PatientNotFoundError extends Error {
    constructor(message: string) {
        super(message);
    }
}

export class PatientDAO_API implements PatientAccessLayer {
    private selectedPatient: Array<Patient>;
    private API_ENDPOINT: string = "http://localhost:8080/api/patients";

    constructor() {
        this.selectedPatient = [];
    }

    public getList(): Array<Patient> {
        return this.selectedPatient;
    }

    public async load(patientVM?: PatientVM): Promise<Array<Patient>> {
        this.selectedPatient = [];
        if (patientVM) {
            const params = new URLSearchParams(); 
            if (patientVM.id) {
                params.append('id', patientVM.id.toString)
            }
        }
        

        return this.selectedPatient;
    }

    public async save(patient: Patient): Promise<void> {
        
    }

    public async delete(item: string | Patient): Promise<void> {
        
    }
}