import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FocusControlDirective } from "./focus.directive";

@NgModule({
    imports: [CommonModule],
    declarations: [FocusControlDirective],
    exports: [FocusControlDirective]
})
export class FocusControlModule {}