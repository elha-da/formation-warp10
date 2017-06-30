export function withIdentity<A>(op: (a: A) => void): (a: A) => A {
  return (a: A) => {
    op(a)
    return a
  }
}