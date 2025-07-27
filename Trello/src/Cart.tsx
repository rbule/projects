import { useContext } from "react";
import Card from "./Card";
import { MoveCardContext } from "./App";
import type {CartProps,orderProps, PendingOrdersProps} from './types'

function Cart({ orders,orderRef }:CartProps){
    const contextMoveCard = useContext(MoveCardContext);

    const moveCard = contextMoveCard?.moveCard;

    return (
        <div className="orderClass">
            <p className="nameText"><strong>Order here</strong></p>
            <div className="orders" ref={orderRef}>
                {orders.map(card => <Card data={card}/>)}
            </div>
            <button onClick={() => {if(moveCard) moveCard(orders[0],'productsBuy');}}>Buy</button>
        </div>
    );
}



function PendingOrders({ pending }: PendingOrdersProps){
    return (
        <div className="pendingOrdersClass">
            <p className="nameText"><strong>Your pending orders</strong></p>
            <div className="pendingOrders">
                {pending.map(card => <Card data={card}/>)}
            </div>
        </div>
    );
}


export default function Order({ type,pending,orders,orderRef }: orderProps){
    if(type === 'cart' && orders && orderRef) return <Cart orders={orders} orderRef={orderRef}/>
    else if(type === 'cart')
        return (
            <div className="orderClass">
                <p className="nameText"><strong>Order here</strong></p>
            </div>
        );
    else if(type === 'pending' && pending) return <PendingOrders pending={pending} />
    else if(type === 'pending')
        return (
            <div className="pendingOrdersClass">
                <p className="nameText"><strong>Your pending orders</strong></p>
            </div>
        );
}