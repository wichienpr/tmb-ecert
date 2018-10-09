import { NgModule } from "@angular/core";
import {
    AccountNumberPipe,
    DateStringPipe,
    DecimalFormatPipe,
    EmptyStringPipe,
    ExponentialStrengthPipe,
    CommaStringBrPipe,
    ThaiMoneyString,
    DatetimeStringPipe
} from "pipes/";

@NgModule({
    imports: [],
    declarations: [
        AccountNumberPipe,
        DateStringPipe,
        DecimalFormatPipe,
        EmptyStringPipe,
        ExponentialStrengthPipe,
        CommaStringBrPipe,
        ThaiMoneyString,
        DatetimeStringPipe
    ],
    exports: [
        AccountNumberPipe,
        DateStringPipe,
        DecimalFormatPipe,
        EmptyStringPipe,
        ExponentialStrengthPipe,
        CommaStringBrPipe,
        ThaiMoneyString,
        DatetimeStringPipe
    ]
})
export class PipesModule { }