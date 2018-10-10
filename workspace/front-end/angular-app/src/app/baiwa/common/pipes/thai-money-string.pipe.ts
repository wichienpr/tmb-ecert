import { Pipe, PipeTransform } from '@angular/core';

// @credit https://github.com/tpsumeta/ThaiBath.git
declare var $;

@Pipe({ name: 'thaiMoneyString' })
export class ThaiMoneyString implements PipeTransform {
    transform(value: string, exponent: string): string {
        let trimstr = $.trim(value);
        if (trimstr == "") {
            return "";
        }else{
            return this.ArabicNumberToText(trimstr);
        }

    }

    CheckNumber(Number: string) {
        var decimal = false;
        Number = Number.toString();
        Number = Number.replace(/ |,|บาท|฿/gi, '');
        for (var i = 0; i < Number.length; i++) {
            if (Number[i] == '.') {
                decimal = true;
            }
        }
        if (decimal == false) {
            Number = Number + '.00';
        }
        return Number
    }
    ArabicNumberToText(numperinput: string) {
        numperinput = this.CheckNumber(numperinput);
        let NumberArray = new Array("ศูนย์", "หนึ่ง", "สอง", "สาม", "สี่", "ห้า", "หก", "เจ็ด", "แปด", "เก้า", "สิบ");
        let DigitArray = new Array("", "สิบ", "ร้อย", "พัน", "หมื่น", "แสน", "ล้าน");
        let BahtText = "";


        let Number: any[] = numperinput.split(".");
        if (Number[1].length > 0) {
            Number[1] = Number[1].substring(0, 2);
        }
        var NumberLen = Number[0].length - 0;
        for (var i = 0; i < NumberLen; i++) {
            let tmp: any = Number[0].substring(i, i + 1) - 0;
            if (tmp != 0) {
                if ((i == (NumberLen - 1)) && (tmp == 1)) {
                    BahtText += "เอ็ด";
                } else
                    if ((i == (NumberLen - 2)) && (tmp == 2)) {
                        BahtText += "ยี่";
                    } else
                        if ((i == (NumberLen - 2)) && (tmp == 1)) {
                            BahtText += "";
                        } else {
                            BahtText += NumberArray[tmp];
                        }
                BahtText += DigitArray[NumberLen - i - 1];
            }
        }
        BahtText += "บาท";
        if ((Number[1] == "0") || (Number[1] == "00")) {
            BahtText += "ถ้วน";
        } else {
            let DecimalLen = Number[1].length - 0;
            for (var i = 0; i < DecimalLen; i++) {
                var tmp = Number[1].substring(i, i + 1) - 0;
                if (tmp != 0) {
                    if ((i == (DecimalLen - 1)) && (tmp == 1)) {
                        BahtText += "เอ็ด";
                    } else
                        if ((i == (DecimalLen - 2)) && (tmp == 2)) {
                            BahtText += "ยี่";
                        } else
                            if ((i == (DecimalLen - 2)) && (tmp == 1)) {
                                BahtText += "";
                            } else {
                                BahtText += NumberArray[tmp];
                            }
                    BahtText += DigitArray[DecimalLen - i - 1];
                }
            }
            BahtText += "สตางค์";
        }
        return BahtText;
    }



}