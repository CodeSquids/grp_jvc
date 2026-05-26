from database import Database

class VisiterModel:
    def __init__(self):
        self.db = Database()
    
    def get_all(self):
        cursor = self.db.get_cursor()
        try:
            cursor.execute("SELECT * FROM Visiter")
            result = cursor.fetchall()
            return result
        except Exception as e:
            print(f"Erreur get_all: {e}")
            return []
        finally:
            cursor.close()
    
    def create(self, n_visiter, n_visiteur, n_site, nbjours, date_visite):
        cursor = self.db.get_cursor()
        try:
            cursor.execute(
                "INSERT INTO Visiter (n_visiter, n_visiteur, n_site, nbjours, date_visite) VALUES (%s, %s, %s, %s, %s)",
                (n_visiter, n_visiteur, n_site, nbjours, date_visite)
            )
            self.db.commit()
            return {'success': True, 'message': 'Visite créée avec succès'}
        except Exception as e:
            return {'success': False, 'message': str(e)}
        finally:
            cursor.close()
    
    def update(self, n_visiter, n_visiteur, n_site, nbjours, date_visite):
        cursor = self.db.get_cursor()
        try:
            cursor.execute(
                "UPDATE Visiter SET n_visiteur = %s, n_site = %s, nbjours = %s, date_visite = %s WHERE n_visiter = %s",
                (n_visiteur, n_site, nbjours, date_visite, n_visiter)
            )
            self.db.commit()
            return {'success': True, 'message': 'Visite mise à jour avec succès'}
        except Exception as e:
            return {'success': False, 'message': str(e)}
        finally:
            cursor.close()
    
    def delete(self, n_visiter):
        cursor = self.db.get_cursor()
        try:
            cursor.execute("DELETE FROM Visiter WHERE n_visiter = %s", (n_visiter,))
            self.db.commit()
            return {'success': True, 'message': 'Visite supprimée avec succès'}
        except Exception as e:
            return {'success': False, 'message': str(e)}
        finally:
            cursor.close()
    
    def get_by_id(self, n_visiter):
        cursor = self.db.get_cursor()
        try:
            cursor.execute("SELECT * FROM Visiter WHERE n_visiter = %s", (n_visiter,))
            result = cursor.fetchone()
            return result
        except Exception as e:
            print(f"Erreur get_by_id: {e}")
            return None
        finally:
            cursor.close()

    # ===================== REQUÊTE 1 : Liste des visiteurs par site et période =====================
    
    def complex1(self, site_nom, date_start, date_end):
        cursor = self.db.get_cursor()
        try:
            query = """
                SELECT 
                    v.n_visiteur,
                    v.nom,
                    v.adresse,
                    s.nom AS nom_site,
                    DATE_FORMAT(vi.date_visite, '%%d/%%m/%%Y') AS date_visite,
                    s.tarif_journalier AS tarif,
                    vi.nbjours,
                    (vi.nbjours * s.tarif_journalier) AS montant
                FROM visiter vi
                JOIN visiteur v ON vi.n_visiteur = v.n_visiteur
                JOIN site s ON vi.n_site = s.n_site
                WHERE vi.date_visite BETWEEN %s AND %s
            """
            params = [date_start, date_end]
            
            if site_nom and site_nom != "Tous les sites":
                query += " AND s.nom = %s"
                params.append(site_nom)
            
            query += " ORDER BY vi.date_visite DESC"
            
            cursor.execute(query, params)
            result = cursor.fetchall()
            return result
        except Exception as e:
            print(f"Erreur complex1: {e}")
            return []
        finally:
            cursor.close()
    
    # ===================== REQUÊTE 2 : Effectif et montant total par site =====================
    
    def complex2(self, date_start, date_end):
        cursor = self.db.get_cursor()
        try:
            query = """
                SELECT 
                    s.n_site,
                    s.nom AS nom_site,
                    COUNT(DISTINCT vi.n_visiteur) AS effectif,
                    COALESCE(SUM(vi.nbjours * s.tarif_journalier), 0) AS montant
                FROM visiter vi
                JOIN site s ON vi.n_site = s.n_site
                WHERE vi.date_visite BETWEEN %s AND %s
                GROUP BY s.n_site, s.nom
                ORDER BY montant DESC
            """
            cursor.execute(query, (date_start, date_end))
            result = cursor.fetchall()
            return result
        except Exception as e:
            print(f"Erreur complex2: {e}")
            return []
        finally:
            cursor.close()
    
    # ===================== REQUÊTE 3 : Liste complète des visiteurs =====================
    
    def complex3(self):
        """Liste complète des visiteurs sans filtre de dates"""
        cursor = self.db.get_cursor()
        try:
            query = """
                SELECT 
                    v.n_visiteur,
                    v.nom,
                    v.adresse,
                    s.nom AS nom_site,
                    DATE_FORMAT(vi.date_visite, '%%d/%%m/%%Y') AS date_visite,
                    s.tarif_journalier AS tarif,
                    vi.nbjours,
                    (vi.nbjours * s.tarif_journalier) AS montant
                FROM visiter vi
                JOIN visiteur v ON vi.n_visiteur = v.n_visiteur
                JOIN site s ON vi.n_site = s.n_site
                ORDER BY vi.date_visite DESC
            """
            cursor.execute(query)
            result = cursor.fetchall()
            return result
        except Exception as e:
            print(f"Erreur complex3: {e}")
            return []
        finally:
            cursor.close()
    
    # ===================== REQUÊTE 4 : Statistiques globales par site =====================
    
    def complex4(self):
        """Statistiques globales par site (toutes périodes confondues)"""
        cursor = self.db.get_cursor()
        try:
            query = """
                SELECT 
                    s.n_site,
                    s.nom AS nom_site,
                    COUNT(DISTINCT vi.n_visiteur) AS effectif,
                    COALESCE(SUM(vi.nbjours * s.tarif_journalier), 0) AS montant
                FROM visiter vi
                JOIN site s ON vi.n_site = s.n_site
                GROUP BY s.n_site, s.nom
                ORDER BY montant DESC
            """
            cursor.execute(query)
            result = cursor.fetchall()
            return result
        except Exception as e:
            print(f"Erreur complex4: {e}")
            return []
        finally:
            cursor.close()