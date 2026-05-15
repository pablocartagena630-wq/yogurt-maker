DELIMITER $$
CREATE FUNCTION fn_nivel_lealtad_cliente(p_id_cliente INT)
RETURNS VARCHAR(20)
DETERMINISTIC
READS SQL DATA
BEGIN
	DECLARE V_total_citas INT;
	DECLARE v_total_gastado DECIMAL(10,2);
	DECLARE V_nivel VARCHAR(20);

SELECT
	COUNT(distinct c.id_cita),
    COALESCE(SUM (dc.precio_aplicado),0)
    INTO V_total_citas,v_total_gastado
	FROM cliente cl
	LEFT JOIN cita c ON cl.id_cliente AND c.estado = 'completado'
	LEFT JOIN detalle_cita dc ON C.id_cita = dc.id_cita
	WHERE id_cliente = p_id_cliente


	IF v_total_gastado >=2000 THEN
		SET v_nivel = 'VIP ORO';
	ELSEIF v_total_gastado >=1000 THEN
		SET v_nivel = 'VIP PLATA';
	ELSEIF v_total_gastado >= 500 THEN
		SET v_nivel = 'VIP BRONCE';
    ELSEIF v_total_gastado >= 5 THEN
		SET v_nivel = 'Cliente frecuente';
	ELSEIF v_total_gastado >= 1 THEN
		SET v_nivel = 'Cliente regular';
	ELSE 
		SET v_nivel = 'Cliente nuevo';
	END IF;
    RETURN v_nivel;
END;
DELIMITER 


SELECT
	nombre,apellido
    fn_nivel_lealtad_cliente(id_cliente) AS nivel_de_la_tabla
FROM cliente 
ORDER BY 

