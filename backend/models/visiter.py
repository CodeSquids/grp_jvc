from database import Database

class VisiterModel:
    def __init__(self):
        self.db = Database()
    
    def get_all(self):
        cursor = self.db.get_cursor()
        cursor.execute("SELECT * FROM Visiter")
        result = cursor.fetchall()
        cursor.close()
        return result
    
    def create(self, n_visiter, n_visiteur, n_site, nbjours, date_visite):
        cursor = self.db.get_cursor()
        try:
            cursor.execute(
                "INSERT INTO Visiter (n_visiter, n_visiteur, n_site, nbjours, date_visite) VALUES (%s, %s, %s, %s, %s)",
                (n_visiter, n_visiteur, n_site, nbjours, date_visite)
            )
            self.db.commit()
            return {'success': True, 'message': 'Visite créé avec succès'}
        except Exception as e:
            return {'success': False, 'message': str(e)}
        finally:
            cursor.close()
    
    def update(self, n_visiter, n_visiteur, n_site, nbjours, date_visite):
        cursor = self.db.get_cursor()
        try:
            cursor.execute(
                "UPDATE Visiter SET n_visiteur = %s, n_site = %s, nbjours= %s, date_visite= %s WHERE n_visiter = %s",
                ( n_visiteur, n_site, nbjours, date_visite ,n_visiter)
            )
            self.db.commit()
            return {'success': True, 'message': 'Visite mis à jour avec succès'}
        except Exception as e:
            return {'success': False, 'message': str(e)}
        finally:
            cursor.close()
    
    def delete(self, n_visiter):
        cursor = self.db.get_cursor()
        try:
            # Vérifier si le visiter a des visites
            cursor.execute("SELECT COUNT(*) as count FROM Visiter WHERE n_visiter = %s", (n_visiter,))
            result = cursor.fetchone()
            if result['count'] > 0:
                return {'success': False, 'message': 'Impossible de supprimer: ce visiter a des visites enregistrées'}
            
            cursor.execute("DELETE FROM Visiter WHERE n_visiter = %s", (n_visiter,))
            self.db.commit()
            return {'success': True, 'message': 'Visiter supprimé avec succès'}
        except Exception as e:
            return {'success': False, 'message': str(e)}
        finally:
            cursor.close()
    
    def get_by_id(self, n_visiter):
        cursor = self.db.get_cursor()
        cursor.execute("SELECT * FROM Visiter WHERE n_visiter = %s", (n_visiter,))
        result = cursor.fetchone()
        cursor.close()
        return result


    def complex1(self, site_nom, date_start, date_end):
        cursor = self.db.get_cursor()
        try:
            query = """
                SELECT 
                    v.n_visiteur,
                    v.nom,
                    v.adresse,
                    s.nom AS nom_site,
                    vi.date_visite,
                    vi.nbjours,
                    vi.nbjours * s.tarif_journalier AS montant
                FROM visiter vi
                JOIN visiteur v 
                    ON vi.n_visiteur = v.n_visiteur
                JOIN site s 
                    ON vi.n_site = s.n_site
                WHERE vi.date_visite BETWEEN %s AND %s
                AND s.nom = %s
            """
            cursor.execute(query, (date_start, date_end, site_nom))
            result = cursor.fetchall()
            return result
        except Exception as e:
            return {'success': False, 'message': str(e)}
        finally:
            cursor.close()
    
    def complex2(self, date_start, date_end):
        cursor = self.db.get_cursor()
        try:
            query = """
                SELECT 
                    s.n_site,
                    s.nom AS nom_site,
                    COUNT(DISTINCT vi.n_visiteur) AS effectif,
                    SUM(vi.nbjours * s.tarif_journalier) AS montant
                FROM visiter vi
                JOIN site s
                    ON vi.n_site = s.n_site
                WHERE vi.date_visite BETWEEN %s AND %s
                GROUP BY s.n_site, s.nom;
            """
            cursor.execute(query, (date_start, date_end))
            result = cursor.fetchall()
            return result
        except Exception as e:
            return {'success': False, 'message': str(e)}
        finally:
            cursor.close() 
    