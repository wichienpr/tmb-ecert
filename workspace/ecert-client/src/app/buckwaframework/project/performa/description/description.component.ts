import { Component, OnInit } from '@angular/core';
declare var $: any;
@Component({
  selector: 'app-description',
  templateUrl: './description.component.html',
  styleUrls: ['./description.component.css']
})
export class DescriptionComponent implements OnInit {

  modal: string[] = ['desp'];

  constructor() { }

  ngOnInit() {
  }

  openModal(id) {
    $(`#${id}`).modal('show');
  }

  closeModal(id) {
    $(`#${id}`).modal('hide');
  }

}
