import { NgModule } from "@angular/core";
import { AccountNumberPipe, DateStringPipe, DecimalFormatPipe, EmptyStringPipe, ExponentialStrengthPipe } from "pipes/";

@NgModule({
    imports: [],
    declarations: [AccountNumberPipe, DateStringPipe, DecimalFormatPipe, EmptyStringPipe, ExponentialStrengthPipe],
    exports: [AccountNumberPipe, DateStringPipe, DecimalFormatPipe, EmptyStringPipe, ExponentialStrengthPipe]
})
export class PipesModule { }