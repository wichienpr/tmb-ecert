import { NgModule } from "@angular/core";
import { AccountNumberPipe, DateStringPipe, DecimalFormatPipe, EmptyStringPipe, ExponentialStrengthPipe ,CommaStringBrPipe } from "pipes/";

@NgModule({
    imports: [],
    declarations: [AccountNumberPipe, DateStringPipe, DecimalFormatPipe, EmptyStringPipe, ExponentialStrengthPipe ,CommaStringBrPipe],
    exports: [AccountNumberPipe, DateStringPipe, DecimalFormatPipe, EmptyStringPipe, ExponentialStrengthPipe ,CommaStringBrPipe]
})
export class PipesModule { }