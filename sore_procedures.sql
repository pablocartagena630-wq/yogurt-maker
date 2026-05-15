DELIMITER $$
CREATE PROCEDURE sp_reporte_mensual(
IN p_anio INT,
IN p_mes INT
)
BEGIN
	-- RESUMEN GENERAL
	SELECT 
		'RESUMEN GENERAL' AS tipo,
        COUNT(distinct c.id_cita) AS total_citas,
        SUM(CASE WHEN c.estado = 'completada'THEN 1 ELSE 0 END) AS completadas,
        SUM(CASE WHEN c.estado = 'cancelada'THEN 1 ELSE 0 END) AS canceladas,
        COUNT(distinct cl) AS clientes_atendidos,
        SUM(dc.precio_aplicado) AS ingresos_totales,
        AVG(dc.precio_aplicado) AS promedio_servicios
    FROM cita c 
    JOIN cliente_cl ON c.id_cliente = c.id_cliente
    JOIN detalle_cita dc ON c.id_cita = c.id_cliente
    WHERE YEAR (c.fecha) = p_anio AND MONTH (c.fecha) = p_mes;
    
    -- INGRESOS POR BARBERO
    SELECT 
    'INGRESOS POR BARBERO' AS tipo,
    CONCAT(b.nombre,' ',b.apellido) AS barbero,
    COUNT(DISTINCT c.id_cita) AS cita,
    SUM(dc.precio_aplicado) AS ingrese,
    AVG(dc.precio_aplicado) AS promedio
    FROM barber b
    LEFT JOIN cita c ON b.id_barbero = c.id_barbero
    AND YEAR(c.fecha) = p_anio
    AND MONTH (c.fecha) = p_mes
    AND c.estado = 'completado'
    LEFT JOIN dtealle_cita dc ON c.id_cita = dc.id_cita
    GROUP BY b.id_barbero;
    
    -- Servicios mas solicitados
    SELECT
		'TOP DE SERVICIOS' AS tipo,
        s.nombre AS servicio,
        COUNT(dc.id_detalle) AS veces_vendido,
        SUM(dc.precio_aplicado) AS ingresos
    FROM servicios s
    JOIN detalle_cita dc ON s.id_servicio = dc.id_servicio
    JOIN cita c ON dc.id_cita = c.id_cita
    WHERE YEAR (c.fecha) = p.anio AND MONTH(c.fecha) = p_mes
		AND c.estado = 'completado'
        GROUP BY s.id_servicio
        ORDER BY veces_vendidos DESC
        LIMIT 5;
END$$
DELIMITTER ;

CALL sp_reporte_mensual(2024,2); 