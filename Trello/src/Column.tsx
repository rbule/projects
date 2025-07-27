import Card from "./Card";
import type {cardType,orderType,ColumnProps,OrderColumnProps,Column} from './types'


function ProductsColumn({cards, setQuery}:ColumnProps){
    return (
        <div className="columnClass">
            <p className="nameText"><strong>Products</strong></p>
            <div className="scrollArea">
                {cards.map((card:cardType) => <Card key={card.id} data={card}/>)}
            </div>
            <input type="text" placeholder="Search" onChange={(e) => setQuery(e.target.value)}/>
        </div>
    );
}

function OrdersColumn({cards, orders}:OrderColumnProps){
    function getData(productId: number): cardType | undefined {
        return cards.find(card => card.id === productId);
    }

    return(
        <div className="columnClass">
            <p className="nameText"><strong>My orders</strong></p>
            <div className="scrollArea">
                {orders.map((order: orderType) => {
                    const product = getData(order.itemId);
                    return product ? <Card key={order.id} data={product} order={order}/> : null;
                })}
            </div>
        </div>
    );
}


export default function Column({ type='placeholder',products,orders,setQuery }: Column) {
    if(type === 'placeholder'){
        return (
            <div className="columnClass">
            </div>
        );
    }
    else if(type === 'loading'){
        return (
            <div className="columnClass">
                <p className="nameText"><strong>Loading...</strong></p>
            </div>
        );
    }
    else if (type === 'products' && setQuery && products) return <ProductsColumn cards={products} setQuery={setQuery}/>;
    else if (type === 'orders' && orders && products) return <OrdersColumn cards={products} orders={orders}/>;
    return null;
}