import { FormGroup } from "@angular/forms";

export interface Dropdown {
    [X: string]: any;
    formGroup: FormGroup;
    formControlName: string;
    type: string;
    dropdownId: string;
    dropdownName: string;
    placehold?: string;
    valueName: string;
    labelName: string;
    values: any[];
    activeValue?: string;
}

export enum DropdownMode{
    "SEARCH" = "search",
    "SELECTION" = "selection",
}