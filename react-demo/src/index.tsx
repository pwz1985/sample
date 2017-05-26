import * as React from "react";
import * as ReactDOM from "react-dom";

import { Router, Route } from "react-router";

import { Cate } from "./components/Cate";

ReactDOM.render(
  <div>
    <Cate compiler="111" framework="121" />
  </div>,
  document.getElementById("example")
);