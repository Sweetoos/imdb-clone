import { Component, input, Input, InputSignal } from '@angular/core';
import { TitleInfo } from '../model/title';

@Component({
  selector: 'app-title-card',
  imports: [],
  templateUrl: './title-card.html',
  styleUrl: './title-card.css',
})
export class TitleCardComponent {
  movie=input.required<TitleInfo>;

}
