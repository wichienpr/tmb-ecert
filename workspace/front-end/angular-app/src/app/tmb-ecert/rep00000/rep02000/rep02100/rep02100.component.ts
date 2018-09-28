import { Component, OnInit } from '@angular/core';
declare var $: any;
@Component({
  selector: 'app-rep02100',
  templateUrl: './rep02100.component.html',
  styleUrls: ['./rep02100.component.css']
})
export class Rep02100Component implements OnInit {

  showData: boolean = false;

  constructor() { }

  ngOnInit() { }
  ngAfterViewInit() { }

  searchData(): void {
    this.showData = true;
  }

  clearData(): void {
    this.showData = false;
  }

}
