import { NgModule } from "@angular/core";
import { AccountNumberPipe, DateStringPipe, DecimalFormatPipe, EmptyStringPipe, ExponentialStrengthPipe ,CommaStringBrPipe } from "pipes/";
import { ThaiMoneyString } from "./thai-money-string.pipe";

@NgModule({
    imports: [],
    declarations: [AccountNumberPipe, DateStringPipe, DecimalFormatPipe, EmptyStringPipe, ExponentialStrengthPipe ,CommaStringBrPipe,ThaiMoneyString],
    exports: [AccountNumberPipe, DateStringPipe, DecimalFormatPipe, EmptyStringPipe, ExponentialStrengthPipe ,CommaStringBrPipe,ThaiMoneyString]
})
export class PipesModule { }