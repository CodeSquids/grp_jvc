from database import Database

class SiteModel:
    def __init__(self):
        self.db = Database()
    
    def get_all(self):
        cursor = self.db.get_cursor()
        cursor.execute("SELECT * FROM Site ORDER BY n_site")
        result = cursor.fetchall()
        cursor.close()
        return result
    
    def create(self, n_site, nom, lieu, tarif_journalier):
        cursor = self.db.get_cursor()
        try:
            cursor.execute(
                "INSERT INTO Site (n_site, nom, lieu, tarif_journalier) VALUES (%s, %s, %s, %s)",
                (n_site, nom, lieu, tarif_journalier)
            )
            self.db.commit()
            return {'success': True, 'message': 'Visite créé avec succès'}
        except Exception as e:
            return {'success': False, 'message': str(e)}
        finally:
            cursor.close()
    
    def update(self, n_site, nom, lieu, tarif_journalier):
        cursor = self.db.get_cursor()
        try:
            cursor.execute(
                "UPDATE Site SET nom = %s, lieu = %s, tarif_journalier= %s, WHERE n_site = %s",
                ( nom, lieu, tarif_journalier ,n_site)
            )
            self.db.commit()
            return {'success': True, 'message': 'Visite mis à jour avec succès'}
        except Exception as e:
            return {'success': False, 'message': str(e)}
        finally:
            cursor.close()
    
    def delete(self, n_site):
        cursor = self.db.get_cursor()
        try:
            # Vérifier si le site a des visites
            cursor.execute("SELECT COUNT(*) as count FROM Site WHERE n_site = %s", (n_site,))
            result = cursor.fetchone()
            if result['count'] > 0:
                return {'success': False, 'message': 'Impossible de supprimer: ce site a des visites enregistrées'}
            
            cursor.execute("DELETE FROM Site WHERE n_site = %s", (n_site,))
            self.db.commit()
            return {'success': True, 'message': 'Site supprimé avec succès'}
        except Exception as e:
            return {'success': False, 'message': str(e)}
        finally:
            cursor.close()
    
    def get_by_id(self, n_site):
        cursor = self.db.get_cursor()
        cursor.execute("SELECT * FROM Site WHERE n_site = %s", (n_site,))
        result = cursor.fetchone()
        cursor.close()
        return result

