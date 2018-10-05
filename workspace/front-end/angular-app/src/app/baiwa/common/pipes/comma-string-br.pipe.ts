import { Pipe, PipeTransform } from '@angular/core';
import { forEach } from '@angular/router/src/utils/collection';
declare var $: any;

@Pipe({ name: 'commaStringBr' })
export class CommaStringBrPipe implements PipeTransform {

  // วิธีใช้ <span *ngFor="let stringlist of stringDefault | commaStringBr ">{{stringlist}}<br></span>  
  transform(value: String): String[] {
    let string = '';
    let stringlist = value.split(",");
    stringlist.forEach(element => {
      string+=(string=="")?element:'<br>'+element;
    });
    return stringlist;
  }
}