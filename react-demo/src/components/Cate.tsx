import * as React from "react";
import './Cate.less';

export interface CateProps { compiler: string; framework: string; }

export class Cate extends React.Component<CateProps, undefined> {
  render() {
    return <div>
      <header className="wx-header">111</header>
      <section className="wx-body">222</section>
      <footer className="wx-footer">3333</footer>
    </div>;
  }
}