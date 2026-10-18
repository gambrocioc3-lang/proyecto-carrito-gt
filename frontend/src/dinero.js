// 2121.86 → "Q2,121.86"
export const dinero = (n) =>
  "Q" + Number(n).toLocaleString("es-GT", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
