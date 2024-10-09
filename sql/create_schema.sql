CREATE TABLE if NOT EXISTS orders (
    uuid UUID PRIMARY KEY,             
    id BIGINT NOT NULL,                
    region VARCHAR(255) NOT NULL,      
    country VARCHAR(255) NOT NULL,     
    item_type VARCHAR(255) NOT NULL,  
    sales_channel VARCHAR(50) NOT NULL,
    priority CHAR(1) NOT NULL,         
    order_date DATE NOT NULL,          
    ship_date DATE NOT NULL,          
    units_sold INT NOT NULL,          
    unit_price NUMERIC(10, 2) NOT NULL,
    unit_cost NUMERIC(10, 2) NOT NULL, 
    total_revenue NUMERIC(15, 2),      
    total_cost NUMERIC(15, 2),         
    total_profit NUMERIC(15, 2),       
    self_link TEXT                     
);
