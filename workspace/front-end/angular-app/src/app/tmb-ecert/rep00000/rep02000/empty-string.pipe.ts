import { Pipe, PipeTransform } from '@angular/core';
declare var $: any;

@Pipe({ name: 'emptyString' })
export class EmptyStringPipe implements PipeTransform {
  transform(value: String): String {
    return $.trim(value) == "" ? "-" : value;
  }
}