module.exports = function(sequelize, DataTypes) {
	var Benificiaries = sequelize.define("beneficiaries", {
		id: {
			type: DataTypes.INTEGER,
			primaryKey: true,
			autoIncrement: true
		},
		account_number: {
			type: DataTypes.INTEGER,
			allowNull: false
		},
		beneficiary_account_number: {
			type: DataTypes.INTEGER,
			allowNull: false
		},
		approved: {
			type: DataTypes.BOOLEAN,
			allowNull: false,
			defaultValue: false
		}
	}, {
		timestamps: false
	});
	return Benificiaries;
};
