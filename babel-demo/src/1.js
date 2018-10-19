const xxx = [1, 2, 3];

const yyy = [...xxx, 4, 5];

class Test {
  xx = 1;
  cxskdjs() {
    this.xx = 2;
    console.log(this.xx);
  }
}

const test = new Test();
test.cxskdjs();
console.log(test.xx);

for (let x of yyy) {
  const zzz = x;
  console.log(zzz + 1);
}
