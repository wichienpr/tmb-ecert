import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { CalendarComponent } from "./calendar.component";
import { ReactiveFormsModule } from "@angular/forms";
import { DisableControlModule } from "app/baiwa/common/directives";

@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        DisableControlModule
    ],
    declarations: [CalendarComponent],
    exports: [CalendarComponent]
})
export class CalendarModule { }