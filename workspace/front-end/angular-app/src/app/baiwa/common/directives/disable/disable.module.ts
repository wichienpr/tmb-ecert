import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { DisableControlDirective } from "./disable.directive";

@NgModule({
    imports: [CommonModule],
    declarations: [DisableControlDirective],
    exports: [DisableControlDirective]
})
export class DisableControlModule {}