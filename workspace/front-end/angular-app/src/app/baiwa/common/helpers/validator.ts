import { FormGroup } from "@angular/forms";

export function isValid(form: FormGroup, control: string, submitted: boolean) {
    if (form.controls[control].invalid && submitted) {
        return false;
    }
    return true;
}