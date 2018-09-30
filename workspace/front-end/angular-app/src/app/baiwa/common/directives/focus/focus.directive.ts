import { Directive, Input, ElementRef } from '@angular/core';

@Directive({
  selector: '[focusControl]'
})
export class FocusControlDirective {

  @Input() set focusControl(condition) {
    const action = condition ? 'focus' : 'blur';
    this.el.nativeElement[action]();
  }

  constructor(private el: ElementRef) { }

}