<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $G_Gal_E_id
 * @property int        $M_News_id
 * @property Date       $G_Gal_E_date
 * @property string     $G_Gal_E_name
 * @property int        $MT_id
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class GGalleryEvents extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'G_Gallery_Events';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'G_Gal_E_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'G_Gal_E_id', 'M_News_id', 'G_Gal_E_date', 'G_Gal_E_name', 'MT_id', 'C_St_id', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'G_Gal_E_id' => 'int', 'M_News_id' => 'int', 'G_Gal_E_date' => 'date', 'G_Gal_E_name' => 'string', 'MT_id' => 'int', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'G_Gal_E_date', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;
    public static function boot()
    {
        parent::boot();

        static::creating(function ($article) {
            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {
            $article->updated_at = now();
        });
    }

    // Scopes...

    // Functions ...

    // Relations ...
    public function eq_gallery()
    {
        return $this->hasMany(GGallery::class, 'G_Gal_E_id');
    }
    public function eq_event_lng()
    {
        return $this->hasMany(GGalleryEventsLng::class, 'G_Gal_E_id');
    }
    public function eq_news()
    {
        return $this->belongsTo(MNews::class, 'M_News_id');
    }
}
