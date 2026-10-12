import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { JsonPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Header } from "./header/header";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header],
  templateUrl:'./app.html',
  styleUrl: './app.css',
})
export class App {

}
