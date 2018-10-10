import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { CalendarComponent } from "./calendar.component";
import { ReactiveFormsModule } from "@angular/forms";
import { DisableControlModule } from "app/baiwa/common/directives";
import { NgCalendarComponent } from "./ng-calendar.component";

@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        DisableControlModule
    ],
    declarations: [CalendarComponent, NgCalendarComponent],
    exports: [CalendarComponent, NgCalendarComponent]
})
export class CalendarModule { }