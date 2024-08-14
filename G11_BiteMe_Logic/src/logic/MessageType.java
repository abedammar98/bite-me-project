package logic;

/**
 * The MessageType enum represents the different types of messages that can be
 * sent between the client and server.
 */
public enum MessageType {
	UserConnect, GetCustomerOrders, GetResturantOrders, GetResturantMenuItems, UpdateResturantMenu, UpdateOrderStatus,
	UpdateItem, GetReport, GetOrder, AddOrderToDB, AddUserToDB, GetResturants, GetBranchOrders, SaveReport,
	ClientConnected, ClientDisconnected, UserDisconnect, UpdateOrderStatusSupplier, AddItemSupplier, DeleteItemSupplier,
	UpdateItemSupplier, GetAllItems, GetOrderItems, GetBranchManagerReportsYearMonth, getBranchReports,
	GetCEOReportsYearQuarter, getQuarterReports1, getQuarterReports2, SupplierRecievedNewOrder;
}
