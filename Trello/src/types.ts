
export type cardType = { id: number; name: string; description: string; price: number; rating: number; quantity: number};
export type orderType = { id:number; itemId:number; buyerId:number; amountBought:number; rating:number }

export type contextType = {
  moveCard: (card:cardType,where?:string) => void
}

export type inCartType = {
  [productId: number]: boolean;
}


export type cardProps = {
    data: cardType,
    order?: orderType,
}

export type orderProps = {
    type: string,
    pending?:cardType[],
    orders?:cardType[],
    orderRef?:React.RefObject<HTMLDivElement | null>  | undefined
}

export type Column = {
    type?: string,
    products?:cardType[]
    orders?:orderType[],
    setQuery?: (query:string) => void,
}

export type OrderColumnProps = {
    cards: cardType[],
    orders: orderType[]
}


export type ColumnProps = {
    cards:cardType[]
    setQuery: (query:string) => void,
}

export type CartProps = {
    orders: cardType[],
    orderRef?: React.RefObject<HTMLDivElement | null> | undefined
}

export type PendingOrdersProps = {
    pending: cardType[]
}

export type HeaderProps = {
  userId: number | null;
  onLogout: () => void;
}