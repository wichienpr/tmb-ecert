import { Component, OnInit } from '@angular/core';
declare var $: any;
@Component({
  selector: 'app-rep02000',
  templateUrl: './rep02000.component.html',
  styleUrls: ['./rep02000.component.css']
})
export class Rep02000Component implements OnInit {

  showData: boolean = false;

  modal: string[] = ["modal"];
  constructor() { }

  ngOnInit() { }
  ngAfterViewInit() {

  }


  openModal(id) {
    $('#' + id).modal('show');
  }

  closeModal(id) {
    $('#' + id).modal('hide');
  }

  searchData(): void {
    this.showData = true;
  }

  clearData(): void {
    this.showData = false;
  }

}
